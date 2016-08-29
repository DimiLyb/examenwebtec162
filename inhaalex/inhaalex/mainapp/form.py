from django import forms

class adding(forms.Form):
    nname = forms.CharField(label='Naam', max_length=100)
    nexamen = forms.CharField(label='Examen', max_length=100)
    ndatum = forms.CharField(label='Datum', max_length=100)
    nreden = forms.CharField(label='Reden', max_length=300)
    