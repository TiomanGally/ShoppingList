docker run -it --rm=true --name shopping_list -e POSTGRES_USER=shopping -e POSTGRES_PASSWORD=list -e POSTGRES_DB=shopping_list -p 5437:5432 postgres:14.1
