FROM openjdk:17
WORKDIR /home/avada/web/slj.avada-media-dev1.od.ua/projects/A.Rudiuk/Coffee-app
ADD target/Coffee_App_A_Rudiuk_Admin.jar .
ENTRYPOINT ["java", "-jar", "Coffee_App_A_Rudiuk_Admin.jar"]