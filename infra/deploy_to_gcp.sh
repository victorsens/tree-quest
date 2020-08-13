cd /tree_guest/k8s/
kustomize edit set image ${PROJECT_IMAGE}$CIRCLE_WORKFLOW_ID
cat kustomization.yml
kustomize build > ./kustomized-deployment.yaml
cat ./kustomized-deployment.yaml
istioctl kube-inject -f ./kustomized-deployment.yaml > istio-deployment.yaml   # convert kubernates deployment to istio;
kubectl apply -f ./istio-deployment.yaml
kubectl rollout status deployment/tree-guest-v1