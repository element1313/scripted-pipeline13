properties([
    parameters([
        string(defaultValue: '', description: 'provide node ip', name: 'node', trim: true
        )]
    )])
node{
    stage("Pull repo"){
        sh "rm -rf ansible-melodi && git clone https://github.com/ikambarov/ansible-melodi.git"
    }
    stage("Install Melodi"){
        ansiblePlaybook become: true, colorized: true, credentialsId: 'jenkins-master', disableHostKeyChecking: true, inventory: "${params.node}",  playbook: 'ansible-melodi/main.yml', sudo: true
    }
}
    