#coding:utf-8
import json
import os
import chardet
path_dir=os.listdir("./baidu")
write_file=open("baidu.txt","w")
for s in path_dir:

    path=os.path.join("./baidu",s)

    file=open(path)

    data=file.read().decode("utf-8")
    data=data.replace(" ","")
    data=data.replace("\r\n","")
    data = data.replace("'", '"')

    # print type(data)

    # print s
    # print data

    dir=json.loads(data)

    # print dir["shop_name"]
    # print dir["shop_logo"]
    # print dir["starting_price"]
    # print dir["deliver_fee"]
    # print len(dir["hot_products"])+len(dir["foods"])
    # for i in dir["hot_products"]:
    #     print i["product_name"]
    #     print i["product_price"]
    #     print i["product_photo"]
    # 
    # for i in dir["foods"]:
    #     print i["food_name"]
    #     print i["food_price"]
    #     print i["food_image"]

    write_file.writelines(str(unicode.encode( dir["shop_name"],"utf-8"))+"\n")
    write_file.writelines(str(unicode.encode( dir["shop_logo"],"utf-8"))+"\n")
    write_file.writelines(str(unicode.encode( dir["starting_price"],"utf-8"))+"\n")
    write_file.writelines(str(unicode.encode( dir["deliver_fee"],"utf-8"))+"\n")
    try:
        write_file.writelines(str(len(dir["hot_products"]) + len(dir["foods"]))+"\n")
    except Exception as e:
        continue
    for i in dir["hot_products"]:
        write_file.writelines(str(unicode.encode( i["product_name"],"utf-8"))+"\n")
        write_file.writelines( str(unicode.encode(i["product_price"],"utf-8"))+"\n")
        write_file.writelines(str(unicode.encode( i["product_photo"],"utf-8"))+"\n")

    for i in dir["foods"]:
        write_file.writelines( str(unicode.encode(i["food_name"],"utf-8"))+"\n")
        write_file.writelines(str(unicode.encode( i["food_price"],"utf-8"))+"\n")
        write_file.writelines(str(unicode.encode( i["food_image"],"utf-8"))+"\n")


    # write_file.writelines(str(unicode.encode(dir["shop_name"],"utf-8")))




    # raw_input()



    # print dir



write_file.close()