properties([
    parameters([
        booleanParam(defaultValue: true, description: 'Do you want to run terraform apply', name: 'terraform_apply'),
        booleanParam(defaultValue: false, description: 'Do you want to run terraform destroy', name: 'terraform_destroy'),
        choice(choices: ['dev', 'qa', 'prod'], description: 'Choose Environment', name: 'environment')
    ])
])





if(params.environment == 'dev') {
    aws_region_var = "us-east-1" 
}
else if(params.environment == 'qa') {
    aws_region_var = "us-east-2" 
}
else if(params.environment == 'prod') {
    aws_region_var = "us-west-2" 
}


def tfvar = """
    s3_bucket = \"shokhrukh-element13\"
    s3_folder_project = \"terraform_vpc\"
    s3_folder_region = \"us-east-1\"
    s3_folder_type = \"class\"
    s3_tfstate_file = \"infrastructure.tfstate\"
    environment = \"${params.environment}\"

    region   = \"${aws_region_var}\"
    az1      = \"${aws_region_var}a\"
    az2      = \"${aws_region_var}b\"
    az3      = \"${aws_region_var}c\"

    vpc_cidr_block  = \"172.32.0.0/16\"

    public_cidr1    = \"172.32.1.0/24\"
    public_cidr2    = \"172.32.2.0/24\"
    public_cidr3    = \"172.32.3.0/24\"

    private_cidr1   = \"172.32.10.0/24\"
    private_cidr2   = \"172.32.11.0/24\"
    private_cidr3   = \"172.32.12.0/24\"



"""

node{
    stage("Pull Repo"){
        cleanWs()
        git branch: 'master', url: 'https://github.com/ikambarov/terraform-ec2.git'
        
        writeFile file: "${params.environment}tf.vars", text: "${tfvar}"
   
    }



    withEnv(["AWS_REGION=${aws_region_var}"]) {
        withCredentials([usernamePassword(credentialsId: 'aws_jenkins-key', passwordVariable: 'AWS_SECRET_ACCESS_KEY', usernameVariable: 'AWS_ACCESS_KEY_ID')]) {
            stage("Terraform Init"){
                sh """
                    bash setenv.sh ${params.environment}.tfvars
                    terraform init 
                """
            }

            if(params.terraform_apply){
                stage("Terraform Apply"){
                    sh """
                        terraform apply -var-file ${params.environment}.tfvars -auto-approve
                    """
                }
            }
            else if(params.terraform_destroy){
                stage("Terraform Destroy"){
                    sh """
                        terraform destroy -var-file ${params.environment}.tfvars -auto-approve
                    """
                }
            }
            else {
                stage("Terraform Plan"){
                    sh """
                        terraform plan -var-file ${params.environment}.tfvars
                    """
                }
            }           
        }
    }         
}