from lxml import etree
import requests
import dbOps
import re
import os
from PIL import Image
from io import BytesIO
import sys
from cos import upload_file_bytes
from time import sleep

url_prefix="https://commons.wikimedia.org"
headers = {'user-agent':'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 \
    (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36'}
# 访问wiki需要代理
proxies = { "http": "http://127.0.0.1:56948", "https": "https://127.0.0.1:56948"}
file_filter = '//div[@class="mw-filepage-resolutioninfo"]//a[@class="mw-thumbnail-link"]/@href'
resolution_filter = '//div[@class="mw-filepage-resolutioninfo"]//a[@class="mw-thumbnail-link"]/text()'
original_file_filter = '//div[@class="fullMedia"]//a[@class="internal"]/@href'
original_resolu_filter = '//div[@class="fullMedia"]//span[@class="fileInfo"]//text()'
pic_title_filter = '//div[@class="hproduct commons-file-information-table"]//span[@style="font-weight:bold"]//text()'
pic_intro_filter = '//div[contains(@class,"description mw-content-ltr")]//text()'
pic_genre_filter = '//div[@class="hproduct commons-file-information-table"]//td[@id="fileinfotpl_art_genre"]/following-sibling::*[1]/a[@class="extiw"]/text()'
pic_tech_filter = '//td[@id="fileinfotpl_art_medium"]/following-sibling::td[1]//a[@class="extiw"]/text()'

update_picture_sql = "update pic set title=%(title)s,url=%(url)s,width=%(width)s,height=%(height)s,intro=%(intro)s,genre=%(genre)s,tech=%(tech)s where pid=%(pid)s"
insert_file_sql = "insert into fileinfo(pid,url,resolution) value(%(pid)s,%(url)s,%(resolution)s)"



def request(url):
    text = requests.get(url=url,headers=headers,proxies=proxies)
    text.encoding="utf-8"
    text=text.text
    return text

def paser(text,filter):
    return etree.HTML(text).xpath(filter)


def extract_file_info(pid,text):
    # 抽取图片各种分辨率的文件信息，并设置图片的默认显示url和尺寸
    file_list = paser(text,file_filter)
    resolution_list = paser(text,resolution_filter)   
    file_info_list = []
    durl = ''
    dwidth=0
    dheight=0

    # 上传文件、插入各分辨率的文件信息
    if len(file_list)!=0:  # 存在多种分辨率
        for i in range(len(file_list)):
            file_name = file_list[i]
            resolu = re.findall(r'[0-9,]+',resolution_list[i]) 
            width = resolu[0].replace(',','')
            height = resolu[1].replace(',','')
            file_info_list.append({"pid":pid,"url":file_name,"resolution":'('+width+', '+height+')'})
            # 设置图片默认显示url和宽高
            if i==0:
                durl = file_name
                dwidth = width
                dheight = height
    else:  # 只有一种分辨率
        file_name = paser(text,original_file_filter)[0]
        resolu = re.findall(r'[0-9,]+',paser(text,original_resolu_filter)[0]) 
        width = resolu[0].replace(',','')
        height = resolu[1].replace(',','')
        # 设置图片默认显示url和宽高
        durl = file_name
        dwidth = width
        dheight = height

    # 将各种分辨率的文件信息存到数据库里
    dbOps.insert_many(insert_file_sql,file_info_list)    
    # 更新picture里对应pid的记录的title,url等详细信息
    title = ' '.join(paser(text,pic_title_filter))
    intro=' '.join(paser(text,pic_intro_filter))
    genre=','.join(paser(text,pic_genre_filter))
    tech=','.join(paser(text,pic_tech_filter))
    dbOps.update_one(update_picture_sql,{"title":title,"url":durl,"width":dwidth,
    "height":dheight,"intro":intro,"genre":genre,"tech":tech,"pid":pid})


if __name__ == "__main__":
    waiti = 0
    stepi = 1
    counti = 0
    offset = 0
    record = dbOps.select_one("select pid,refer from pic where url='' and pid>108000 limit "+str(offset)+",1 ",[])
    while record!=None:
        try:
            sleep(waiti)
            extract_file_info(record['pid'],request(url_prefix+record['refer']))
            counti = counti+1
            if waiti>0 and counti>=3:
                waiti=waiti-stepi
                counti=0
            print("finished "+str(record['pid']))
        except Exception as e:
            offset+=1
            print(e)
            counti=0
            waiti = waiti+stepi
            print("current wait interval: "+str(waiti))
        finally:
            record = dbOps.select_one("select pid,refer from pic where url=''  and pid>108000 limit "+str(offset)+",1",[])
