

curl --location 'http://10.10.10.61:9999/test' \
--header 'Content-Type: application/json' \
--data '{
"outputFormat": "pdf",
"d": {
"picture": "static_map_picture.png",
"title": "Тестовый отчёт по участку 123",
"column": "Тестовое описание участка",
"value": "Здесь может быть любая текстовая информация.",
"crs": "EPSG:7828",
"area": 12345.67,
"coords": [
{
"num": 1,
"x": 1.0,
"y": 1.0
},
{
"num": 2,
"x": 4924048.5002,
"y": 4388829.67
}
]
}
}'