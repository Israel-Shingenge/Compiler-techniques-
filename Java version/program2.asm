; -- header --
bits 64
default rel

section .bss
read_number resq 1

section .data
read_format db "%d", 0
string_literal_0 db "not equal", 0
string_literal_1 db "equal", 0

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
	mov rax, 19
	push rax
	; -- PUSH --
	mov rax, 19
	push rax
	; -- SUB --
	pop rax
	pop rbx
	sub rbx, rax
	push rbx
	; -- JUMP.EQ.0 --
	pop rax
	cmp rax, 0
	je L1
	; -- PRINT --
	lea rcx, string_literal_0
	xor eax, eax
	call printf
	; -- HALT --
	jmp EXIT_LABEL
L1:
	; -- PRINT --
	lea rcx, string_literal_1
	xor eax, eax
	call printf
	; -- HALT --
	jmp EXIT_LABEL
EXIT_LABEL:
	xor rax, rax
	call ExitProcess
