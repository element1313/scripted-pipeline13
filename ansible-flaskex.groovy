properties([
    parameters([
        string(defaultValue: '', description: 'provide node ip', name: 'node', trim: true
        )]
    )])
node{
    stage("Pull Repo"){
        git url: 'https://github.com/ikambarov/ansible-Flaskex.git'
    }
    stage("Install prerequisites"){
        ansiblePlaybook credentialsId: 'jenkins-master', disableHostKeyChecking: true, inventory: "${params.node}",  playbook: 'prerequisites.yml'
    }
    withEnv(['FLASKEX_REPO=https://github.com/element1313/flaskex.git', 'FLASKEX_BRANCH=master']) {
       stage("Pull Repo"){
          ansiblePlaybook become: true, colorized: true, credentialsId: 'jenkins-master', disableHostKeyChecking: true, inventory: "${params.node}",  playbook: 'pull_repo.yml'
       }
    }
     
     stage("Install Python"){
        ansiblePlaybook become: true, colorized: true, credentialsId: 'jenkins-master', disableHostKeyChecking: true, inventory: "${params.node}",  playbook: 'install_python.yml'
    }
      stage("start application"){
        ansiblePlaybook become: true, colorized: true, credentialsId: 'jenkins-master', disableHostKeyChecking: true, inventory: "${params.node}",  playbook: 'start_app.yml'
    }
}