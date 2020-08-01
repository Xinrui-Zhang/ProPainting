from lxml import etree
import requests
import dbOps
import test
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
proxies = { "http": "http://127.0.0.1:56948", "https": "http://127.0.0.1:56948"}
file_filter = '//div[@class="mw-filepage-resolutioninfo"]//a[@class="mw-thumbnail-link"]/@href'
# resolution_filter = '//div[@class="mw-filepage-resolutioninfo"]//a[@class="mw-thumbnail-link"]/text()'
original_file_filter = '//div[@class="fullMedia"]//a[@class="internal"]/@href'
pic_title_filter = '//div[@class="hproduct commons-file-information-table"]//span[@style="font-weight:bold"]//text()'
pic_intro_filter = '//div[contains(@class,"description mw-content-ltr")]//text()'
pic_genre_filter = '//div[@class="hproduct commons-file-information-table"]//td[@id="fileinfotpl_art_genre"]/following-sibling::*[1]/a[@class="extiw"]/text()'
pic_tech_filter = '//td[@id="fileinfotpl_art_medium"]/following-sibling::td[1]//a[@class="extiw"]/text()'

update_picture_sql = "update pic set title=%(title)s,url=%(url)s,width=%(width)s,height=%(height)s,intro=%(intro)s,genre=%(genre)s,tech=%(tech)s where pid=%(pid)s"
insert_file_sql = "insert into fileinfo(pid,url,resolution,size,format) value(%(pid)s,%(url)s,%(resolution)s,%(size)s,%(format)s)"

waiti = 0
stepi = 2
counti = 0


def request(url):
    text = requests.get(url=url,headers=headers,proxies=proxies)
    text.encoding="utf-8"
    text=text.text
    return text

def paser(text,filter):
    return etree.HTML(text).xpath(filter)


def extract_file_info(pid,text):
    global waiti,counti,stepi
    # 抽取图片各种分辨率的文件信息，并设置图片的默认显示url和尺寸
    file_list = paser(text,file_filter)
    
    file_info_list = []
    url = ''
    width=0
    height=0

    # 上传文件、插入各分辨率的文件信息
    if len(file_list)!=0:  # 存在多种分辨率
        
        for i in range(len(file_list)):
            sleep(waiti) 
            img_bytes = BytesIO(requests.get(file_list[i]).content)

            if waiti>0 and counti>=3:
                waiti=waiti-stepi
                counti=0

            img = Image.open(img_bytes)
            
            file_name = 'picture/'+ str(pid)+'_'+str(img.size[0])+'_'+str(img.size[1])+re.findall(r'\.[a-zA-Z]+$',file_list[i])[0]
            
            file_info_list.append({"pid":pid,"url":file_name,
            "resolution":str(img.size),"size":sys.getsizeof(img_bytes),"format":img.format})
            # 将图片文件上传到腾讯云对象存储
            upload_file_bytes(img_bytes, file_name)
            # 设置图片默认显示url和宽高
            if i==0:
                url = file_name
                width = img.size[0]
                height = img.size[1]
    else:  # 只有一种分辨率
        file_url = paser(text,original_file_filter)[0]

        sleep(waiti) 
        img_bytes = BytesIO(requests.get(file_url).content)
        if waiti>0 and counti>=3:
            waiti=waiti-stepi
            counti=0

        img = Image.open(img_bytes)
        file_name = 'picture/'+str(pid)+'_'+str(img.size[0])+'_'+str(img.size[1])+re.findall(r'\.[a-zA-Z]+$',file_url)[0]
        file_info_list.append({"pid":pid,"url":file_name,
        "resolution":str(img.size),"size":sys.getsizeof(img_bytes),"format":img.format})
        # 将图片文件上传到腾讯云对象存储
        upload_file_bytes(img_bytes, file_name)
        # 设置图片默认显示url和宽高
        url = file_name
        width = img.size[0]
        height = img.size[1]

    # 将各种分辨率的文件信息存到数据库里
    dbOps.insert_many(insert_file_sql,file_info_list)    
    # 更新picture里对应pid的记录的title,url等详细信息
    title = ','.join(paser(text,pic_title_filter))
    intro=','.join(paser(text,pic_intro_filter))
    genre=','.join(paser(text,pic_genre_filter))
    tech=','.join(paser(text,pic_tech_filter))
    dbOps.update_one(update_picture_sql,{"title":title,"url":url,"width":width,
    "height":height,"intro":intro,"genre":genre,"tech":tech,"pid":pid})


if __name__ == "__main__":
    offset = 0
    record = dbOps.select_one("select pid,refer from pic where url='' limit "+str(offset)+",1",[])
    while record!=None:
        try:
            extract_file_info(record['pid'],request(url_prefix+record['refer']))
            print("finished "+str(record['pid']))
        except Exception as e:
            offset+=1
            print(e)
            counti=0
            waiti = waiti+stepi
            print("current wait interval: "+str(waiti))
        finally:
            record = dbOps.select_one("select pid,refer from pic where url='' limit "+str(offset)+",1",[])
