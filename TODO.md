#TODO
- cache
- event driven (streaming)
- batching
- ajout CI/CD

#Command utils
minikube start --memory=7837 --cpus=4
kubectl port-forward svc/minio 9001:9001
mvn spring-boot:build-image -DskipTests

kubectl delete pods --all -n default
kubectl delete svc --all -n default
kubectl delete deployment --all -n default
kubectl delete deployment,svc,statefulset,pod -l app=loki -n default
kubectl delete pod -l app=loki --all-namespaces --force --grace-period=0
kubectl delete statefulset loki -n default
kubectl port-forward service/kafka 9092:9092
kubectl delete deployment kafka kafka-ui

#Service namespace
exchange:8000
conversion:8100
list-properties:8200
eureka:8761
spring-cloud:8888
api-gateway:8765
zipkin:9411
loki:3100
prometheus:9090
tempo:4317/4318/3200
minio:9000/9001
grafana:3000
keycloak:8080 (admin/admin)
kafka-ui:8282
kafka: 9092

https://www.geeksforgeeks.org/springboot/spring-boot-rabbitmq-configuration/