node("docker"){
    stage("Pull Repo"){
        git url: 'https://github.com/ikambarov/Flaskex-docker.git'
    }
    stage("Docker Build"){
        sh "docker build -t ${REGISTRY_USERNAME}/flaskex2 . "
    }
    withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', passwordVariable: 'REGISTRY_PASSWORD', usernameVariable: 'REGISTRY_USERNAME')]) {
        stage("Docker Login"){
            sh "docker login -u ${REGISTRY_USERNAME} -p '${REGISTRY_PASSWORD}'"
        }
    }
    stage("Docker Push"){
        sh "docker push s${REGISTRY_USERNAME}/flaskex2"
    }
}