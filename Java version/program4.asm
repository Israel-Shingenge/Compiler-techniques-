; -- header --
bits 64
default rel

section .bss
read_number resq 1

section .data
read_format db "%d", 0
string_literal_0 db "Negative", 0
string_literal_1 db "Not Divisible by 3", 0
string_literal_2 db "Divisible by 3", 0
string_literal_3 db "Zero", 0

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
	mov rax, 5
	push rax
	; -- JUMP.EQ.0 --
	pop rax
	cmp rax, 0
	je L2
	; -- JUMP.GT.0 --
	pop rax
	cmp rax, 0
	jg Le
	; -- PRINT --
	lea rcx, string_literal_0
	xor eax, eax
	call printf
	; -- HALT --
	jmp EXIT_LABEL
Le:
	; -- PUSH --
	mov rax, 7
	push rax
	; -- MOD --
	pop rbx
	pop rax
	xor rdx, rdx
	idiv rbx
	push rdx
	; -- JUMP.EQ.0 --
	pop rax
	cmp rax, 0
	je L1
	; -- PRINT --
	lea rcx, string_literal_1
	xor eax, eax
	call printf
	; -- HALT --
	jmp EXIT_LABEL
L1:
	; -- PRINT --
	lea rcx, string_literal_2
	xor eax, eax
	call printf
	; -- HALT --
	jmp EXIT_LABEL
L2:
	; -- PRINT --
	lea rcx, string_literal_3
	xor eax, eax
	call printf
	; -- HALT --
	jmp EXIT_LABEL
EXIT_LABEL:
	xor rax, rax
	call ExitProcess
