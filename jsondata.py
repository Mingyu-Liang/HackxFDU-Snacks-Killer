import json
with open('dongbeimingzufeng.txt', 'r') as f:
	all_file = f.read()
	dict_file = eval(all_file)
	dict_file = unicode(dict_file)
	json_data = json.dumps(dict_file)
	print json_data