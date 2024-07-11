# FROM node:latest as build

# WORKDIR /usr/src/app

# COPY package*.json ./

# RUN npm install

# RUN npm install -g @angular/cli

# COPY . .

# # RUN ng build --configuration=production
# RUN npm run build

# RUN ls -la /usr/src/app/dist

FROM nginx:latest

COPY nginx.conf /etc/nginx/nginx.conf

COPY /dist/auction-frontend/browser /usr/share/nginx/html

EXPOSE 80