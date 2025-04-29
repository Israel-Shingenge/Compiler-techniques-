; -- header --
bits 64
default rel

section .bss
read_number resq 1

section .data
read_format db "%d", 0
string_literal_0 db "Hello, World", 0

section .text
global main
extern ExitProcess
extern printf
extern scanf

main:
    push rbp
    mov rbp, rsp
    sub rsp, 32

	; -- PUSH --
	mov rax, 10
	push rax
	; -- PUSH --
	mov rax, 7
	push rax
	; -- ADD --
	pop rax
	pop rbx
	add rbx, rax
	push rbx
	; -- PRINT --
	lea rcx, string_literal_0
	xor eax, eax
	call printf
	; -- HALT --
	jmp EXIT_LABEL
EXIT_LABEL:
	xor rax, rax
	call ExitProcess
