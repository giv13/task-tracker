FROM nginx:alpine

COPY css/ /usr/share/nginx/html/css/
COPY img/ /usr/share/nginx/html/img/
COPY js/ /usr/share/nginx/html/js/
COPY plugins/ /usr/share/nginx/html/plugins/
COPY *.html /usr/share/nginx/html/
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
