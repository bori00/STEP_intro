function displayComments(){
    fetch('/comments-data')
    .catch(error => console.log('failed to fetch comments data from server'))
    .then(response => response.json())
    .catch(error => console.log('failed to parse comments'))
    .then(response => addAllCommentsToDOM(response))
    .catch(error => console.log('failed to print comments to DOM'));
}

function createCardHolderElement(){
    var cardHolder = document.createElement("div");
    cardHolder.setAttribute('class', 'card-holder-small');
    return cardHolder;
}

function createCardElement(){
    var card = document.createElement("div");
    card.setAttribute('class', 'card-small');
    return card;
}

function createParagraphElement(comment){
    var paragraph = document.createElement("p"); 
    paragraph.innerText = comment;
    return paragraph;
}

function addCommentToDOM(comment){
    console.log("displaying comment " + comment)
    var cardHolder = createCardHolderElement();
    var card = createCardElement();
    var paragraph = createParagraphElement(comment);
    card.appendChild(paragraph);
    cardHolder.appendChild(card);
    var main = document.getElementById("main-comments");        
    main.appendChild(cardHolder);  
}

function addAllCommentsToDOM(comments){
    console.log("add all comments to DOM")
    for(var i=0; i<comments.length; i++){
        addCommentToDOM(comments[i]);
    }
}