## Instruções para mandar modificaçes feitas no código do App Android

Primeira coisa a se fazer, clonar o projeto do GitHub, para isso deves abrir o git no seu terminal primeiro, crie uma nova pasta para por o projeto nela  e abra o Git nessa pasta

1. Iniciar o Git no seu terminal
```bash
git init
```

2. Clonar o projeto
```bash
git clone https://github.com/YesLap/Android.git
```

Abrir o projeto e fazer as mudaças que você quiser, depois de ter feito todas as suas mudanças e quiser mandar o projeto para o GitHub seguir os seguintes passos no Git:

1. Criar uma nova branch
```bash
git checkout -b "nomedabranch" (para seguir um padrão colocar o nome da seguinte maneira, edit-nomedaactivity-seunome)
```

2. Depois basta dar uma add para adcioanr todos os arquivos
```bash
git add . (não esqueça de colocar o ponto)
```

3. E em seguida dar um commit
```bash
git commit -m "versaodocommit" (esta versão voce tera que chegar la no repositorio do GitHub [Releases](https://github.com/YesLap/Android/releases) qual foi a ultima versao lançada e acrescentar uma a mais, ex, se a ultima for 1.0.9 voce devera colcoar 1.0.10)
```

4. Depois disso deverá voltar para a branch master
```bash
git checkout master
```

5. Depois disso iremos usar o rebase para juntar sua branch com a branch master
```bash
git rebase nomedasuabranch (ex: edit-userprofile-rodrigo)
git pull --rebase origin master
git push origin master
```

6. Depois disso acrescentar uma tag com o que foi feito no projeto
```bash
git tag -a 1.0.0 -m "mensagem" (aquele 1.0.0 devera ser substituido pela versao que voce commitou, e a mensagem devera descrever o que voce editou no projeto)
```

7. Enviar a tag para o GitHub
```bash
git push origin master --tags
```
