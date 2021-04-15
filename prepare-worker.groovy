properties([
    parameters([
        string(defaultValue: '', description: 'Input node IP ', name: 'SSHNODE', trim: true)
        ])
    ])

node {
   withCredentials([sshUserPrivateKey(credentialsId: 'jenkins-master', keyFileVariable: 'SSHKEY', passphraseVariable: '', usernameVariable: 'SSHUSERNAME')]) {
      stage("Initialize") {
         sh "ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${ params.SSHNODE }  yum install epel-release -y"
      }
      stage("Install Java") {
         sh "ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${ params.SSHNODE }  yum install java-1.8.0-openjdk-devel -y"
      }
      stage("Install git") {
         sh "ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${ params.SSHNODE }  yum install git -y"
      }
    //   stage("Install maven") {
    //      sh "ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${ params.SSHNODE }  yum install maven -y"
    //   }
    //   stage("updating package repository") {
    //      sh "ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${ params.SSHNODE }  yum makecache"
    //   }
      stage("Install Ansible") {
         sh "ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${ params.SSHNODE }  yum install ansible -y"
      }
      stage("Install Terraform") {
         sh "ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${ params.SSHNODE }  'yum install wget unzip -y && wget https://releases.hashicorp.com/terraform/0.13.1/terraform_0.13.1_linux_amd64.zip && unzip -o terraform_0.13.1_linux_amd64.zip -d /usr/bin/'"
      }
      stage("Install Packer") {
            sh "ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${ params.SSHNODE } 'yum install -y yum-utils && yum-config-manager --add-repo https://rpm.releases.hashicorp.com/RHEL/hashicorp.repo && mv /usr/sbin/packer /usr/sbin/packer_original && yum -y install packer'"
      }
   }
}