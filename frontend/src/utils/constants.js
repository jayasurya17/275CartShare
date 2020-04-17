module.exports = {
	BACKEND_SERVER: {
        URL: 'http://localhost:9000', // Should have http://
    },
    USER_INFORMATION: {
        USER_ID: localStorage.getItem('275UserId'),
        USER_TYPE: localStorage.getItem('275UserType'),
    }
}