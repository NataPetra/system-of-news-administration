FROM swaggerapi/swagger-ui:latest

ADD /news-comments-service/spec/news-comments-service.yml /usr/share/nginx/html/doc/news-comments-service.yml
ADD /other/spec/news-comments.yml /usr/share/nginx/html/doc/news-comments.yml

ENV URLS="[{ url: 'doc/news-comments-service.yml', name: 'News'}, { url: 'doc/news-comments.yml', name: 'Some'}]"