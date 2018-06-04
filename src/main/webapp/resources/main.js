/**
 * comment를 경고창에 출력 후 path에 해당하는 경로로 이동
 * @param path
 * @param comment
 * @returns
 */
function goBack(path, comment) {
	if (arguments.length == 2) alert(comment);
	window.location.href = path;
}
/**
 * id의 value 값을 반환
 * @param id
 * @returns
 */
function getIdValue(id) {
	return document.getElementById(id).value;
}
/**
 * id의 Object 반환
 * @param id
 * @returns
 */
function getIdObject(id) {
	return document.getElementById(id);
}