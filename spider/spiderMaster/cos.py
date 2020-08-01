# -*- coding=utf-8
# appid 已在配置中移除,请在参数 Bucket 中带上 appid。Bucket 由 BucketName-APPID 组成
# 1. 设置用户配置, 包括 secretId，secretKey 以及 Region
from qcloud_cos import CosConfig
from qcloud_cos import CosS3Client
import sys
import logging
import requests
from PIL import Image
from io import BytesIO

logging.basicConfig(level=logging.INFO, stream=sys.stdout)

secret_id = 'AKIDILbbKoF1kCOO8c9rc70cHlz0mJZY6p8A'      # 替换为用户的 secretId
secret_key = 'xf8KFtLXR8ZaKoa6NPkgMoztXtymKEvL'      # 替换为用户的 secretKey
region = 'ap-nanjing'     # 替换为用户的 Region
config = CosConfig(Region=region, SecretId=secret_id, SecretKey=secret_key)
# 2. 获取客户端对象
client = CosS3Client(config)

def upload_file_bytes(fbytes,name):
    #### 字节流简单上传
    # response = 
    client.put_object(
    Bucket='propainting-1301790570',
    Body=fbytes,
    Key=name,
    EnableMD5=False
    )
    # print(response['ETag'])

if __name__ == "__main__":

    img_bytes = BytesIO(requests.get("https://upload.wikimedia.org/wikipedia/commons/thumb/c/cd/Almeida_J%C3%BAnior_-_Fuga_para_o_Egito_%28estudo%29.jpg/424px-Almeida_J%C3%BAnior_-_Fuga_para_o_Egito_%28estudo%29.jpg").content)
    upload_file_bytes(img_bytes,'picture/test.jpg')
    print(client.list_objects(
    Bucket='propainting-1301790570',
    Prefix='picture'
))