import random
import re
from time import sleep

import requests
from lxml import etree

import dbOps

url_prefix="https://commons.wikimedia.org"
headers = {'user-agent':'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 \
    (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36'}
# 访问wiki需要代理
proxies = { "http": "http://127.0.0.1:56948", "https": "http://127.0.0.1:56948"}
artist_url_filter = '//div[@class="CategoryTreeItem"]//a/@href'
artist_name_filter= '//div[@class="CategoryTreeItem"]//a/text()'
img_url_filter = '//div[@class="gallerytext"]//a/@href'
img_title_filter = '//div[@class="gallerytext"]//a/text()'
next_artist_page_filter = '//div[@id="mw-subcategories"]/a[contains(text(),"next")]/@href'
next_pic_page_filter = '//div[@id="mw-category-media"]/a[contains(text(),"next")]/@href'
pic_urls = set()
waiti = 1
stepi = 2
counti = 0


def add_to_set(item):
    global pic_urls
    pic_urls.add(item)

def request(url):
    text = requests.get(url=url,headers=headers,proxies=proxies)
    text.encoding="utf-8"
    text=text.text
    return text

def paser(text,filter):
    return etree.HTML(text).xpath(filter)

# 图片refer信息、author信息、资源集信息
def extract_picture_wiki_link(root_url,depth):        # 根url为paintings_by_....形式
    global waiti,counti,stepi
    if depth < 20:
        error = True
        while error==True:
            try:
                sleep(waiti)
                text = request(root_url)
            
                counti = counti+1
                if waiti>1 and counti>=3:
                    waiti=waiti-stepi
                    counti=0
                
                next_pic_page_url = []
                next_pic_page_url.append(root_url[len(url_prefix):])
                print(next_pic_page_url)
                while len(next_pic_page_url) != 0:
                    sleep(waiti)
                    # print(url_prefix+next_pic_page_url[0])
                    pic_page = request(url_prefix+next_pic_page_url[0])
                    
                    url_list = paser(pic_page,img_url_filter)
                    # print(url_list)
                    list(map(add_to_set,url_list))
                    next_pic_page_url = paser(pic_page,next_pic_page_filter)

                subcat_urls = paser(text,artist_url_filter)
                for url in subcat_urls:
                    extract_picture_wiki_link(url_prefix+url,depth+1)
                error = False
            except Exception as e:
                print(e)
                
                counti=0
                waiti = waiti+stepi
                print("current wait interval: "+str(waiti))
                continue

def extract_artist_list():
    artist_url_list=[]
    artist_name_list=[]
    next_artist_page_url = ["/w/index.php?title=Category:Paintings_by_artist&from=A"]
    
    while len(next_artist_page_url)!=0:
        artist_page = request(url_prefix+next_artist_page_url[0])
        artist_url_list += paser(artist_page,artist_url_filter)
        artist_name_list += paser(artist_page,artist_name_filter)
        next_artist_page_url = paser(artist_page,next_artist_page_filter)
    
    artist_insert = "insert into artist(name,url,state) values(%(name)s,%(url)s,'0')"
    args =  [{"name":i[13:],"url":j} for i,j in zip(artist_name_list, artist_url_list)]
    dbOps.insert_many(artist_insert,args)




if __name__ == "__main__":
   
    picture_insert = "insert into pic (artist, refer) values(%(artist)s,%(refer)s)";
    record = dbOps.select_one("select * from artist where state=0 limit 1",[])
    
    while record != None:
        pic_urls.clear()
        extract_picture_wiki_link(url_prefix+record['url'],1)
        
        
        # 将某艺术家对应的所有作品的wiki页面链接插入到数据库里
        artist = record['name']
        
        args = []
        # print(pic_urls)
        for url in list(pic_urls):
            args.append({"artist":artist,"refer":url})
        cid = dbOps.insert_one("insert into collection(title) values(%(title)s)",{"title":artist})
        # print(args)
        dbOps.insert_many(picture_insert,args)
        ids = dbOps.select_all("select pid from pic where artist=%(artist)s",{"artist":artist})
        args = [{"cid":cid,"id":i['pid']} for i in ids]
        # print(args)
        dbOps.insert_many("insert into collectmap(cid,type,id) values(%(cid)s,'1',%(id)s)",args)
        
        dbOps.update_one("update artist set state=1 where name=%(artist)s",{"artist":artist})
        record = dbOps.select_one("select * from artist where state=0 limit 1",[])
        
        print("finished "+artist)
