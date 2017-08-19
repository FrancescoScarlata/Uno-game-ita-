class Mano
{
	private Carta head;
	
	public Mano()
	{
		head=null;
	}
	
	public boolean isEmpty()
	{ 
		return (head == null ? true: false);
	}
	
	public void insertTail(Carta card)
	{ 
		if (isEmpty() )
		{
			card.setNext(head);	
			head = card;
		}
		else
		{
			Carta aux = head;
			for( ; aux.getNext() != null; aux = aux.getNext());
			aux.setNext(card);
		}
	}
	
	public Carta getHead()
	{
		if(isEmpty())
		{
			System.out.println("Mano e' vuota!");
		}
		return head;
	}
	
	public Carta discardC(Carta scelta)
	{
		if (isEmpty())
		{
			System.out.println("La carta non c'e'! O.o");
			return null;
		}
		else
		{ 
			Carta aux = head;
			Carta prev = null;
			for( ; (aux != null && aux.getNext() != null) &&((aux.getNumero() != scelta.getNumero())||(aux.getColore() != scelta.getColore()));
			 prev = aux, aux = aux.getNext());
			if (aux != null)
				if(prev == null)
					head = head.getNext();
				else
					prev.setNext(aux.getNext());
			return aux;
		}
	
	}
	public void svuotaMano()
	{
		head=null;
	}
	
}
