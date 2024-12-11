/*  async function submitForms() {
            try {
                 Lấy dữ liệu form signup
                const signupForm = document.getElementById('signup');
                const formData = new FormData(signupForm);

               // Gửi dữ liệu form signup bằng fetch
                const response = await fetch(signupForm.action, {
                    method: 'POST',
                    body: formData,
                });

                if (response.ok) {
                    const result = await response.json();
                    if (result.success) {
                        // Nếu signup thành công, submit form register
                        document.getElementById('register').submit();
                    } else {
                        // Xử lý lỗi từ phản hồi của server
                        alert('Signup failed: ' + result.message);
                    }
                } else {
                    // Xử lý lỗi HTTP
                    alert('An error occurred during the signup process.');
                }
            } catch (error) {
                // Xử lý lỗi khác
                console.error('Error:', error);
                alert('An unexpected error occurred.');
            }
        }*/