import sys
import re
from time import sleep
from lxml import etree
import requests
from requests.exceptions import ConnectionError


proxies = {"https": "https://127.0.0.1:56948"}

inputfile = "./todl.txt"



url_prefix="https://commons.wikimedia.org"

headers = {'user-agent':'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 \
    (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36'}

file_filter = '//div[@class="mw-filepage-resolutioninfo"]//a[@class="mw-thumbnail-link"]/@href'
resolution_filter = '//div[@class="mw-filepage-resolutioninfo"]//a[@class="mw-thumbnail-link"]/text()'
original_file_filter = '//div[@class="fullMedia"]//a[@class="internal"]/@href'
original_resolu_filter = '//div[@class="fullMedia"]//span[@class="fileInfo"]//text()'
pic_title_filter = '//div[@class="hproduct commons-file-information-table"]//span[@style="font-weight:bold"]//text()'
pic_intro_filter = '//div[contains(@class,"description mw-content-ltr")]//text()'
pic_genre_filter = '//div[@class="hproduct commons-file-information-table"]//td[@id="fileinfotpl_art_genre"]/following-sibling::*[1]/a[@class="extiw"]/text()'
pic_tech_filter = '//td[@id="fileinfotpl_art_medium"]/following-sibling::td[1]//a[@class="extiw"]/text()'

def request(url):
    text = requests.get(url=url,headers=headers ,proxies=proxies)
    text.encoding="utf-8"
    text=text.text
    return text

def paser(text,filter):
    return etree.HTML(text).xpath(filter)

 # 抽取图片详细信息、各种分辨率的文件信息，并设置图片的默认显示url和尺寸
def extract_file_info(pid,text):
    file_list = paser(text,file_filter)
    resolution_list = paser(text,resolution_filter)   
    durl = ''
    dwidth=0
    dheight=0
    file_info = []
    picture_info = ''
    if(len(file_list)==0): # 说明只存在一种分辨率，要用特殊的提取规则
        file_list.append(paser(text,original_file_filter)[0])
        resolution_list.append(paser(text,original_resolu_filter)[0])
    # 提取各分辨率的文件信息
    for i in range(len(file_list)):
        file_name = file_list[i]
        resolu = re.findall(r'[0-9,]+',resolution_list[i]) 
        width = resolu[0].replace(',','')
        height = resolu[1].replace(',','')
        resolution = '('+width+', '+height+')'
        file_info.append('{0}\t{1}\t{2}'.format(pid,file_name,resolution))
        # 设置图片默认显示url和宽高
        if i==0:
            durl = file_name
            dwidth = width
            dheight = height
    # 提取图片的详细信息
    title = ' '.join(paser(text,pic_title_filter))
    intro=' '.join(paser(text,pic_intro_filter))
    genre=' '.join(paser(text,pic_genre_filter))
    tech=' '.join(paser(text,pic_tech_filter))
    picture_info = '{0}\t{1}\t{2}\t{3}\t{4}\t{5}\t{6}\t{7}'.format(pid,title,durl,dwidth,dheight,intro,genre,tech).replace("\n"," ")
    return (file_info,picture_info)

def update_io_file(picinfo,fileinfo,error_lines,lines):
    picinfo.flush()
    fileinfo.flush()
    # 更新剩余的待提取图片列表
    with open(inputfile,"w",encoding="utf-8") as f:
        for line in error_lines:
            f.write(line) 
        for line in lines:
            f.write(line)
        f.flush()


if __name__ == "__main__":
    waiti = 0
    stepi = 1
    counti = 0
    lines = []
    error_lines = [] # 存放那些因网络等原因造成异常而被跳过的图片记录

    run = 0
    with open(inputfile,"r",encoding="utf-8") as f:
        lines = f.readlines()
    
    with open("./fileinfo.txt","a",encoding="utf-8") as fileinfo:
        with open("./pictureinfo.txt","a",encoding="utf-8") as picinfo:
            while lines:
                record = lines.pop().strip("\n").split("\t")
                run+=1
                try:
                    sleep(waiti)
                    file_infos,picture_info = extract_file_info(record[0],request(record[1]))
                    for file_info in file_infos:
                        fileinfo.write(file_info+"\n")
                    picinfo.write(picture_info+"\n")
                    # 调整request间隔
                    counti = counti+1
                    if waiti>0 and counti>=3:
                        waiti=waiti-stepi
                        counti=0
                    # 每隔100次循环：更新一下剩余待提取的图片列表、将已提取的信息显式写回硬盘
                    if run>=100:
                        run=0
                        update_io_file(picinfo,fileinfo,error_lines,lines)
                    print("finished "+str(record[0]))
                except ConnectionError as e:
                    print(e)
                    # 调整request间隔
                    counti=0
                    waiti = waiti+stepi
                    error_lines.append('\t'.join(record)+'\n')
                    print("current wait interval: "+str(waiti))
                except KeyboardInterrupt as e:
                    print("将结果保存到磁盘中......")
                    lines.append('\t'.join(record)+'\n')
                    update_io_file(picinfo,fileinfo,error_lines,lines)
                    print("已保存，程序终止。")
                    sys.exit()
                except BaseException as e:
                    print(e)
            # 循环结束后，将被跳过的图片记录写入输入图片列表、将已提取的信息写回硬盘
            update_io_file(picinfo,fileinfo,error_lines,lines)