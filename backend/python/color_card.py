from sklearn.cluster import KMeans as KM
import numpy as np
from PIL import Image
import os
import sys, getopt
import requests
from io import BytesIO

def rgbToHex(r,g,b):
    return str.upper(hex(((r<<16)|(g<<8)|b))).replace('0X','#')

def getrgbAndHex(rgb_list):
    rgbs=[]
    hexs=[]
    for rgb in rgb_list:
        rgbs.append(','.join([str(i) for i in rgb]))
        hexs.append(rgbToHex(rgb[0],rgb[1],rgb[2])) 
    zipped = zip(rgbs,hexs)
    sort_zipped = sorted(zipped,key=lambda x:(x[1]))
    result = [','.join(i) for i in sort_zipped]
    return '|'.join(result)

def extractColors(stream,maxColor):
    pixData = np.array(stream)
    h, w, d = pixData.shape
    data = np.reshape(pixData,(h*w, d))
    km = KM(n_clusters=maxColor)
    km.fit(data)
    theme = np.array(km.cluster_centers_, dtype=np.uint8).tolist()
    return getrgbAndHex(theme)

def main(argv):
    url = ''
    try:
        opts, args = getopt.getopt(argv,"i:",[])
    except getopt.GetoptError:
        sys.exit(2)
    for opt, arg in opts:
        if opt == '-i':
            url = arg
    
    if url.startswith('http'):
        response = requests.get(url)
        inputFile = BytesIO(response.content)
    else:
        inputFile = url
    
    with Image.open(inputFile) as stream:
            print(extractColors(stream.convert("RGB"),4))

if __name__ == "__main__":
    main(sys.argv[1:])
    
