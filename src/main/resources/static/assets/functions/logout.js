$('#logoutLink').on("click", function (e) {
    e.preventDefault();
    $('#logoutModal').modal('show');
})
function logout(){
    window.location = $('#logoutLink').attr('href');
}