// Script para alternar o menu lateral
document.getElementById('menu-toggle').addEventListener('click', () => {
    const isMenuOpen = sidebar.style.left === '0px';
    document.getElementById('sidebar').style.left = isMenuOpen ? '-240px' : '0';
});
