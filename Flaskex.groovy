properties([
    parameters([
        string(defaultValue: '', description: 'Input node ip', name: 'SSHNODE', trim: true)
        ])
    ])
node{
    withCredentials([sshUserPrivateKey(credentialsId: 'jenkins-master', keyFileVariable: 'SSHKEY', passphraseVariable: '', usernameVariable: 'SSHUSERNAME')]) {
       stage('cloning the repo') {
          sh "ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${ params.SSHNODE }  git clone https://github.com/anfederico/Flaskex"
       }
       stage('changing directory') {
       sh "ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${ params.SSHNODE }  cd Flaskex"
       }
      stage('installing requirements.txt') {
          sh "ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${ params.SSHNODE }  pip3 install -r requirements.txt"
       }
       stage('installing the app') {
          sh "ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${ params.SSHNODE }  python app.py"
       }
    }
}
