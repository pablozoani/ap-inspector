FROM node:15.3 as build

COPY package.json package-lock.json /app/
WORKDIR /app
RUN npm install

ARG REACT_APP_API_URL
COPY src /app/src/
COPY public /app/public/
RUN npm run build

FROM nginx:1.19

COPY --from=build /app/build /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d
CMD ["nginx", "-g", "daemon off;"]
