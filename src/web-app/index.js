const weatherForm = document.querySelector(".weatherSelector")
const cityInput = document.querySelector(".cityInput")
const card = document.querySelector(".card")

weatherForm.addEventListener("submit", event =>{
    event.preventDefault();
    const city = cityInput.value;
    if(city){
        try{
            const weatherData = getWeatherDataFromJavaCode(city);
        } catch(error){
            console.error(error);
            displayError(error);
        }
    } else{
        displayError("Enter city");
    }
})
async function getWeatherDataFromJavaCode(city){

}
function displayweatherInfo(data){

}
function getWeatherEmoji(weather_type){

}
function displayError(messege){
    const errorDisplay = document.createElement("p");
    errorDisplay.textContent = messege;
    errorDisplay.classList.add("errorDisplay");
    card.textContent = "";
    card.style.display = "flex";
    card.appendChild(errorDisplay);
}