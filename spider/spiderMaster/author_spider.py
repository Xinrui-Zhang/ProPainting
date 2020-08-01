import requests
import re

headers = {'user-agent':'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 \
    (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36'}
# 访问wiki需要代理
proxies = { "http": "http://127.0.0.1:56948", "https": "http://127.0.0.1:56948"}

def getAuthorList():
    # 从wikimedia commons的Category:Painters by name网页
    # 把所有作者的名称爬下，保存到author.txt中
    url_list={"https://commons.wikimedia.org/wiki/Category:Painters_by_name",
    'https://commons.wikimedia.org/w/index.php?title=Category:Painters_by_name&subcatfrom=Gose%2C+Xavier%0AXavier+Gos%C3%A9+i+Rovira#mw-subcategories',
    'https://commons.wikimedia.org/w/index.php?title=Category:Painters_by_name&subcatfrom=Pieneman%2C+Jan+Willem%0AJan+Willem+Pieneman#mw-subcategories'}
    
    with open('./author.txt','w',encoding="utf-8") as f:
        for url in url_list:
            text = requests.get(url=url,headers=headers,proxies=proxies)
            text.encoding='utf-8'
            text = text.text
            re_author = re.compile(r'.*</span> <a href="/wiki/Category:([\w_()%.-]+)" title="Category:[\w ()\'.-]+">([\w ()\'.-]+)</a>',re.UNICODE)
            i=0
            for line in text.splitlines():
                try:
                    groups =re_author.match(line).groups()
                    f.write(groups[0])
                    f.write('\t')
                    f.write(groups[1])
                    f.write('\n')
                    i=i+1
                except:
                    continue
            print(i)


if __name__=='__main__':
    getAuthorList()

