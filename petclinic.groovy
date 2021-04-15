properties([
    parameters([
        string(defaultValue: '', description: 'Input node IP ', name: 'SSHNODE', trim: true)
        ])
    ])

node {
   withCredentials([sshUserPrivateKey(credentialsId: 'jenkins-master', keyFileVariable: 'SSHKEY', passphraseVariable: '', usernameVariable: 'SSHUSERNAME')]) {
      stage("cloning the repo") {
         sh "ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${ params.SSHNODE }  git clone https://github.com/spring-projects/spring-petclinic.git"
      }
      stage("changing the directory") {
         sh "ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${ params.SSHNODE }  cd spring-petclinic"
      }
      stage("running the package") {
         sh "ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${ params.SSHNODE }  ./mvnw package"
      }
      stage("installing the app") {
         sh "ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${ params.SSHNODE }  java -jar target/*.jar"
      }
   }
}