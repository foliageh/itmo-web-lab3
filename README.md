# Lab work #3
**Implementation of web application from [lab work #2](https://github.com/foliageh/itmo-web-lab2) using Jakarta EE 10 Faces + Hibernate.**  
Jakarta EE 10 (JSF, CDI, JPA, EJB), Hibernate (PostgreSQL), PrimeFaces, Lombok, WildFly, JavaScript.

> [How to create, launch and deploy to Helios?](#create-launch--deploy)

![image](https://github.com/foliageh/itmo-web-lab3/assets/46216950/e693c4be-89e6-4e86-a282-1e0d9249d0fe)

## Task
Разработать приложение на базе JavaServer Faces Framework, которое осуществляет проверку попадания точки в заданную область на координатной плоскости.

![task](https://github.com/foliageh/itmo-web-lab3/assets/46216950/91415f9b-f89d-4500-ac74-8b200cbf12a0)

Приложение должно включать в себя 2 facelets-шаблона - стартовую страницу и основную страницу приложения, а также набор управляемых бинов (managed beans), реализующих логику на стороне сервера.

**Стартовая страница должна содержать следующие элементы:**
- "Шапку", содержащую ФИО студента, номер группы и номер варианта.
- Интерактивные часы, показывающие текущие дату и время, обновляющиеся раз в 13 секунд.
- Ссылку, позволяющую перейти на основную страницу приложения.

**Основная страница приложения должна содержать следующие элементы:**
- Набор компонентов для задания координат точки и радиуса области в соответствии с вариантом задания. Может потребоваться использование дополнительных библиотек компонентов - ICEfaces (префикс "ace") и PrimeFaces (префикс "p"). Если компонент допускает ввод заведомо некорректных данных (таких, например, как буквы в координатах точки или отрицательный радиус), то приложение должно осуществлять их валидацию.
- Динамически обновляемую картинку, изображающую область на координатной плоскости в соответствии с номером варианта и точки, координаты которых были заданы пользователем. Клик по картинке должен инициировать сценарий, осуществляющий определение координат новой точки и отправку их на сервер для проверки её попадания в область. Цвет точек должен зависить от факта попадания / непопадания в область. Смена радиуса также должна инициировать перерисовку картинки.
- Таблицу со списком результатов предыдущих проверок.
- Ссылку, позволяющую вернуться на стартовую страницу.

**Дополнительные требования к приложению:**
- Все результаты проверки должны сохраняться в базе данных под управлением СУБД PostgreSQL.
- Для доступа к БД необходимо использовать ORM Hibernate.
- Для управления списком результатов должен использоваться Session-scoped Managed Bean.

## Additional Task
Создать кастомный тэг **ducky**, который будет заменять вхождения в содержимое тэга слова _duck_ на гифку с уткой.

Тэг должен иметь следующие атрибуты:
1. **ducksShown**: boolean; определяет, будет ли производиться замена.
2. **minValue**: int; определяет, какое минимальное количество "уток" должно быть в тексте, чтобы производилась замена.

Создать страницу, которая будет содержать этот тэг с текстом, а также элементы управления его атрибутами.

## Create, Launch & Deploy
В этом гайде пошагово разберем, как настроить Jakarta EE 10, WildFly и Hibernate для сборки и запуска проекта,
а также как всё это задеплоить на Helios. Итак, нам понадобятся следующие ингредиенты:
- IntelliJ IDEA (в качестве бесплатной альтернативы можно использовать Eclipse IDE, в целом шаги будут похожи).
В IntelliJ IDEA советую дополнительно установить все плагины, в названии которых есть JSF)
- Java 17 (позже, однако, нам придется указать 11 версию, т.к. новые Helios не поддерживает)
- Jakarta EE 10
- WildFly 30

### Первые шаги
1. Установить SDK для Java 17 (я использую Amazon Corretto). Удобно это сделать прямо из IntelliJ IDEA.
2. Добавить [переменную JAVA_HOME](https://confluence.atlassian.com/doc/setting-the-java_home-variable-in-windows-8895.html).
Это понадобится нам для локального запуска сервера WildFly.
3. Скачать [WildFly 30](https://github.com/wildfly/wildfly/releases/download/30.0.0.Final/wildfly-30.0.0.Final.zip).
Распаковать в корень файловой системы (`C:/`);
4. В cmd выполним скрипт `C:\wildfly-30.0.0.Final\bin\add-user.bat`.
Выбираем `Management User` -> `Username : admin` -> `Update the existing user password and roles`.
5. Запустим `standalone.bat` и убедимся, что по адресу http://127.0.0.1:8080/ нет никаких ошибок.
6. Выполним `jboss-cli.bat --connect command=:shutdown`, чтобы выключить сервер.
Больше нам вручную его запускать не понадобиться, за нас всё сделает IDEA.

### Создание проекта
1. Создаем проект в IDEA: `New Project` -> `Jakarta EE`. 
В `Template` выбираем `Web application`, в `Application server` указываем путь к установленному ранее WildFly. 
В качестве системы сборки выбираем `Maven`. Нажимаем `Next`.
2. В открывшемся окне выбираем `Jakarta EE 10`. Также можно отметить галочками JSF, Servlet и Hibernate, 
но это не особо важно, все равно потом мы укажем зависимости самостоятельно. Нажимаем `Create`.
3. Проект создался. Сгенерированный IDEA сервлет и JSP-страницу удаляем, они нам не нужны. 
Вместо них добавим файлы [webapp/WEB-INF/faces-config.xml](src/main/webapp/WEB-INF/faces-config.xml) и [webapp/WEB-INF/beans.xml](src/main/webapp/WEB-INF/beans.xml).
4. Изменим автоматически сгенерированное содержимое `pom.xml` в соответствии с [примером](pom.xml).
5. Также изменим в соответствии с [примером](src/main/webapp/WEB-INF/web.xml) содержимое `webapp/WEB-INF/web.xml`. 
Я добавил в конфигурацию несколько необязательных, но очень полезных параметров, рекомендую их оставить. 
6. Congratulations, наконец мы можем начать писать код!
Для сборки выполняем `mvn clean install` (удобно это сделать прямо в IDEA, из панели справа), 
а дальше остается запустить сервер, нажав на зеленый треугольник в верхней панели.

### Подключение БД
Будем использовать PostgreSQL и Hibernate, причем к БД подключимся удаленно на Helios. 
Для PostgreSQL, установленного локально, все аналогично. Если вы хотите использовать другую СУБД или ORM,
шаги не будут сильно отличаться, но для нюансов скорее всего понадобится заглянуть в документацию вашей СУБД или провайдера JPA.
1. Для начала нужно немного настроить WildFly. В cmd введем несколько несложных команд.
Файл `postgresql-module.xml`, путь к которому надо указать в первой команде, можно взять [отсюда](extensions/postgresql-module.xml).
И не забудьте изменить `<your-username>` и `<your-password>` на свои логин и пароль от БД.

    ```shell
    jboss-cli.bat "embed-server, module add --name=org.postgresql.jdbc --module-xml=postgresql-module.xml"
    jboss-cli.bat "embed-server --server-config=standalone.xml, /subsystem=datasources/jdbc-driver=postgresql:add(driver-name=postgresql,driver-module-name=org.postgresql.jdbc,driver-xa-datasource-class-name=org.postgresql.xa.PGXADataSource)"
    jboss-cli.bat "embed-server --server-config=standalone.xml, xa-data-source add --name=PostgresDS --driver-name=postgresql --jndi-name=java:jboss/datasources/PostgresDS --user-name=<your-username> --password=<your-password> --xa-datasource-properties=ServerName=localhost, /subsystem=datasources/xa-data-source=PostgresDS/xa-datasource-properties=PortNumber:add(value=5432), /subsystem=datasources/xa-data-source=PostgresDS/xa-datasource-properties=DatabaseName:add(value=studs)"
    ```
3. Добавим в проект файл [resources/META-INF/persistence.xml](src/main/resources/META-INF/persistence.xml). Не забудьте поменять логин и пароль.
4. Пробросим порты от PostgreSQL с Helios на локальный компьютер, чтобы взаимодействовать с СУБД так, 
будто она установлена локально. Например, через [PuTTY](https://www.chiark.greenend.org.uk/~sgtatham/putty/latest.html). Порт 5432 на локалке должен быть открыт.

    ```shell
    plink.exe -batch -ssh sXXXXXX@se.ifmo.ru -pw password -P 2222 -L 5432:pg:5432
    ```
6. Ура, теперь мы можем написать полноценное приложение с б~~лэкджеком~~ базой данных и запускать его на своем компьютере! 
Остался последний шаг - деплой на Helios.

### Деплой на Helios
1. Тот самый архив WildFly, который мы скачивали по ссылке [выше](#первые-шаги),
закинем в свою домашнюю директорию на Helios и выполним команду `unzip -qq wildfly-30.0.0.Final.zip`.
2. Отредактируем файл `wildfly-30.0.0.Final\standalone\configuration\standalone.xml`. 
- Изменить
  
  ``` xml
  <interface name="public">
      <inet-address value="${jboss.bind.address:127.0.0.1}" />
  </interface>
  ```
  на
  
  ``` xml
  <interface name="public">
      <any-address/>
  </interface>
  ```  
- И изменить порты в соответствии со своим portbase следующим образом:
  
  ``` xml
  <socket-binding name="ajp" port="${jboss.ajp.port:8009}"/>
  <socket-binding name="http" port="${jboss.http.port:8080}"/>
  <socket-binding name="https" port="${jboss.https.port:8443}"/>
  <socket-binding name="management-http" interface="management" port="${jboss.management.http.port:9990}"/>
  <socket-binding name="management-https" interface="management" port="${jboss.management.https.port:9993}"/>
  ```  
  изменяем на (для примера положим portbase=22288)
  
  ``` xml
  <socket-binding name="ajp" port="${jboss.ajp.port:22289}"/>
  <socket-binding name="http" port="${jboss.http.port:22288}"/>
  <socket-binding name="https" port="${jboss.https.port:22290}"/>
  <socket-binding name="management-http" interface="management" port="${jboss.management.http.port:22291}"/>
  <socket-binding name="management-https" interface="management" port="${jboss.management.https.port:22292}"/>
  ``` 
3. Наконец введем знакомые команды, не забыв поменять username, password и путь к [postgresql-module.xml](extensions/postgresql-module.xml):
   
    ```shell
    bash ~/wildfly-30.0.0.Final/bin/jboss-cli.sh "embed-server, module add --name=org.postgresql.jdbc --module-xml=postgresql-module.xml"
    bash ~/wildfly-30.0.0.Final/bin/jboss-cli.sh "embed-server --server-config=standalone.xml, /subsystem=datasources/jdbc-driver=postgresql:add(driver-name=postgresql,driver-module-name=org.postgresql.jdbc,driver-xa-datasource-class-name=org.postgresql.xa.PGXADataSource)"
    bash ~/wildfly-30.0.0.Final/bin/jboss-cli.sh "embed-server --server-config=standalone.xml, xa-data-source add --name=PostgresDS --driver-name=postgresql --jndi-name=java:jboss/datasources/PostgresDS --user-name=<your-username> --password=<your-password> --xa-datasource-properties=ServerName=localhost, /subsystem=datasources/xa-data-source=PostgresDS/xa-datasource-properties=PortNumber:add(value=5432), /subsystem=datasources/xa-data-source=PostgresDS/xa-datasource-properties=DatabaseName:add(value=studs)"
    ```
Теперь, когда сервер WildFly настроен, можно приступить непосредственно к деплою:
1. Собрать написанное приложение через `mvn clean install`. В папке проекта `target/` появился файл `lab3.war`.
Закинем его на Helios в папку `~/wildfly-30.0.0.Final/standalone/deployments/`.
2. Запустим сервер командой `bash ~/wildfly-21.0.0.Final/bin/standalone.sh`.
   Если серверу будет не хватать памяти для запуска, на Helios можно выполнить команду `killall -u sXXXXXX`, например.
3. Прокинем порты с Helios на локалку, например, командой `plink.exe -batch -ssh sXXXXXX@se.ifmo.ru -pw password -P 2222 -L 22288:helios.cs.ifmo.ru:22288`.
4. Введем в браузере http://localhost:22288/lab3.
5. ???
6. Ураааа, всё работает!) 🎉🎉🎉
> Обратите внимание, что необязательно перезапускать сервер после каждого обновления lab3.war. Если WildFly запущен,
то при загрузке билда в deployments изменения подхватятся автоматически, и достаточно будет просто обновить страничку в браузере.
