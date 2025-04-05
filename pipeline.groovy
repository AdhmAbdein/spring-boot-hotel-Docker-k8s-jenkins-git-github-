pipeline{
    agent {label 'node2'}
    
    environment{
        docker_img_name = 'adhmabdein/springhotel'
    }
    
    stages{
        stage('checkout'){
            steps{
                script{
                    git "https://github.com/AdhmAbdein/spring-hotel.git"
                }
            }
        }
        stage('log in to docker hub'){
            steps{
                script{
                    withCredentials ([usernamePassword(credentialsId:'docker_hub',usernameVariable:'d_hub_usr',passwordVariable:'d_hub_pass')]){
                        sh 'docker login -u ${d_hub_usr} -p ${d_hub_pass}'
                    }
                }
            }
        }
        stage('CI - Build docker image'){
            steps{
                script{
                   sh 'docker build -t ${docker_img_name} -f Dockerfile .' 
                }
            }
        }
        stage('push image to docker hub'){
            steps{
                script{
                    sh 'docker push ${docker_img_name}'
                }
            }
        }
        stage('CD - Deploy in k8s'){
            steps{
                script{
                       sh 'kubectl apply -f k8s-postgres-pv.yml'
                       sh 'kubectl apply -f k8s-postgres-pvc.yml'
                       sh 'kubectl apply -f k8s-postgres-secret.yml'
                       sh 'kubectl apply -f k8s-postgres-deploy.yml'
                       sh 'kubectl apply -f k8s-postgres-svc.yml'

                       sh 'kubectl apply -f k8s-spring-hotel-deploy.yml'
                       sh 'kubectl apply -f k8s-spring-hotel-svc.yml'
                }
            }
        }


    }
}