# 打包
autoconfig打包命令
mvn clean -Dautoconfig.userProperties=../antx.properties -Dautoconfig.interactive=auto -Dmaven.test.skip=true install
mvn clean -Dautoconfig.userProperties=../antx-product.properties -Dautoconfig.interactive=auto -Dmaven.test.skip=true install
