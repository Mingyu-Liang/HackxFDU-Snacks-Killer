from cloudant.client import Cloudant
from cloudant.error import CloudantException
from cloudant.result import Result, ResultByKey
import json

client = Cloudant("a8f31867-fcec-416e-980f-53db840e205a-bluemix", "fd85cb0c134b390b192c0c8839139accf5ae84abee91fe43e3b5d1e83a5a7808", url="https://a8f31867-fcec-416e-980f-53db840e205a-bluemix:fd85cb0c134b390b192c0c8839139accf5ae84abee91fe43e3b5d1e83a5a7808@a8f31867-fcec-416e-980f-53db840e205a-bluemix.cloudant.com")
client.connect()

databaseName='baidu'

myDatabase = client.create_database(databaseName)

with open('daxueluhanguoliaoli.txt', 'r') as f:
    all_file = f.read()
    jsonDocument = eval(all_file)
newDocument = myDatabase.create_document(jsonDocument)

with open('dongbeimingzufeng.txt', 'r') as f:
    all_file = f.read()
    jsonDocument = eval(all_file)
newDocument = myDatabase.create_document(jsonDocument)

with open('haoqihanbao.txt', 'r') as f:
    all_file = f.read()
    jsonDocument = eval(all_file)
newDocument = myDatabase.create_document(jsonDocument)

with open('jipaizhuyi.txt', 'r') as f:
    all_file = f.read()
    jsonDocument = eval(all_file)
newDocument = myDatabase.create_document(jsonDocument)

with open('kanggetuzhurou.txt', 'r') as f:
    all_file = f.read()
    jsonDocument = eval(all_file)
newDocument = myDatabase.create_document(jsonDocument)

with open('mayutaomalatang.txt', 'r') as f:
    all_file = f.read()
    jsonDocument = eval(all_file)
newDocument = myDatabase.create_document(jsonDocument)

with open('shuhuangjigongbao.txt', 'r') as f:
    all_file = f.read()
    jsonDocument = eval(all_file)
newDocument = myDatabase.create_document(jsonDocument)

with open('woyabiandang.txt', 'r') as f:
    all_file = f.read()
    jsonDocument = eval(all_file)
newDocument = myDatabase.create_document(jsonDocument)

with open('yangmingyuhuangmenjimifan.txt', 'r') as f:
    all_file = f.read()
    jsonDocument = eval(all_file)
newDocument = myDatabase.create_document(jsonDocument)

with open('youcningming.txt', 'r') as f:
    all_file = f.read()
    jsonDocument = eval(all_file)
newDocument = myDatabase.create_document(jsonDocument)

client.disconnect()